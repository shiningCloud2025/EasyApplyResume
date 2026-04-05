import React, { useMemo } from 'react'
import classNames from 'classnames'
import { sanitizeRichTextHtml } from '@utils/content'
import './RichTextContent.scss'

interface RichTextContentProps {
  html?: string
  className?: string
}

const RichTextContent: React.FC<RichTextContentProps> = ({ html, className }) => {
  const safeHtml = useMemo(() => sanitizeRichTextHtml(html), [html])

  if (!safeHtml) {
    return null
  }

  return (
    <div
      className={classNames('rich-text-content', className)}
      dangerouslySetInnerHTML={{ __html: safeHtml }}
    />
  )
}

export default RichTextContent
