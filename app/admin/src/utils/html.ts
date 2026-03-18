const RICH_TEXT_TAG_REGEXP = /<\/?[a-z][\w-]*(?:\s[^>]*)?>/i

const escapeRichTextHtml = (value: string) => {
  return value
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;')
    .replace(/'/g, '&#39;')
}

const buildRichTextFallbackHtml = (value: string) => {
  const normalized = value.replace(/\r\n/g, '\n').trim()
  if (!normalized) return ''

  return normalized
    .split(/\n{2,}/)
    .map((paragraph) => `<p>${escapeRichTextHtml(paragraph).replace(/\n/g, '<br>')}</p>`)
    .join('')
}

/**
 * 富文本 HTML 安全清洗
 */
export const sanitizeRichTextHtml = (value: string) => {
  const html = (value || '').trim()
  if (!html) return ''

  if (typeof DOMParser === 'undefined') {
    return html
      .replace(/<script[\s\S]*?<\/script>/gi, '')
      .replace(/<style[\s\S]*?<\/style>/gi, '')
      .replace(/\son[a-z]+\s*=\s*(['"]).*?\1/gi, '')
      .replace(/\s(href|src)\s*=\s*(['"])\s*javascript:[\s\S]*?\2/gi, '')
  }

  const doc = new DOMParser().parseFromString(html, 'text/html')
  doc.querySelectorAll('script, style, iframe, object, embed, link, meta, base').forEach((element) => {
    element.remove()
  })

  doc.body.querySelectorAll('*').forEach((element) => {
    Array.from(element.attributes).forEach((attr) => {
      const name = attr.name.toLowerCase()
      const attrValue = attr.value.trim()

      if (name.startsWith('on')) {
        element.removeAttribute(attr.name)
        return
      }

      if ((name === 'href' || name === 'src') && /^javascript:/i.test(attrValue)) {
        element.removeAttribute(attr.name)
      }
    })
  })

  return doc.body.innerHTML
}

/**
 * 将富文本内容标准化为可安全预览的 HTML
 */
export const normalizeRichTextHtml = (value: string) => {
  const normalized = (value || '').trim()
  if (!normalized) return ''

  return RICH_TEXT_TAG_REGEXP.test(normalized)
    ? sanitizeRichTextHtml(normalized)
    : buildRichTextFallbackHtml(normalized)
}

/**
 * 提取富文本中的纯文本内容
 */
export const extractRichTextPlainText = (value: string) => {
  const normalized = normalizeRichTextHtml(value)
  if (!normalized) return ''

  if (typeof DOMParser === 'undefined') {
    return normalized.replace(/<[^>]+>/g, ' ').replace(/&nbsp;/gi, ' ').replace(/\s+/g, ' ').trim()
  }

  const doc = new DOMParser().parseFromString(normalized, 'text/html')
  return (doc.body.textContent || '').replace(/\u00a0/g, ' ').replace(/\s+/g, ' ').trim()
}

/**
 * 判断富文本是否包含有效内容
 */
export const hasMeaningfulRichText = (value: string) => {
  const normalized = normalizeRichTextHtml(value)
  if (!normalized) return false
  if (/<img\b/i.test(normalized)) return true
  return extractRichTextPlainText(normalized).length > 0
}
