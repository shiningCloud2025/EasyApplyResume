import dayjs from 'dayjs'

const DISALLOWED_TAGS = [
  'script',
  'style',
  'iframe',
  'object',
  'embed',
  'form',
  'input',
  'button',
  'textarea',
  'select',
  'option',
  'link',
  'meta'
]

const UNSAFE_URL_PATTERN = /^(?:javascript:|vbscript:|data:text\/html)/i
const URL_ATTRIBUTES = new Set(['href', 'src', 'xlink:href', 'formaction'])

export const formatContentDate = (value?: string, format = 'YYYY-MM-DD HH:mm'): string => {
  if (!value) return ''

  const parsed = dayjs(value)
  if (parsed.isValid()) {
    return parsed.format(format)
  }

  return value.replace('T', ' ')
}

export const stripHtml = (html?: string): string => {
  if (!html) return ''

  if (typeof window === 'undefined') {
    return html.replace(/<[^>]*>/g, ' ').replace(/\s+/g, ' ').trim()
  }

  const doc = new DOMParser().parseFromString(html, 'text/html')
  return doc.body.textContent?.replace(/\s+/g, ' ').trim() || ''
}

export const getTextPreview = (html?: string, maxLength = 140): string => {
  const text = stripHtml(html)

  if (!text) return ''
  if (text.length <= maxLength) return text

  return `${text.slice(0, maxLength).trimEnd()}...`
}

export const sanitizeRichTextHtml = (html?: string): string => {
  if (!html) return ''
  if (typeof window === 'undefined') return html

  const doc = new DOMParser().parseFromString(html, 'text/html')
  doc.querySelectorAll(DISALLOWED_TAGS.join(',')).forEach((element) => element.remove())

  const walker = doc.createTreeWalker(doc.body, NodeFilter.SHOW_ELEMENT)
  const elements: Element[] = []
  let currentNode = walker.nextNode()

  while (currentNode) {
    elements.push(currentNode as Element)
    currentNode = walker.nextNode()
  }

  elements.forEach((element) => {
    Array.from(element.attributes).forEach((attribute) => {
      const attributeName = attribute.name.toLowerCase()
      const attributeValue = attribute.value.trim()

      if (attributeName.startsWith('on') || attributeName === 'srcdoc') {
        element.removeAttribute(attribute.name)
        return
      }

      if (URL_ATTRIBUTES.has(attributeName) && UNSAFE_URL_PATTERN.test(attributeValue)) {
        element.removeAttribute(attribute.name)
      }
    })

    if (element.tagName.toLowerCase() === 'a' && element.getAttribute('target') === '_blank') {
      element.setAttribute('rel', 'noopener noreferrer')
    }
  })

  return doc.body.innerHTML
}
