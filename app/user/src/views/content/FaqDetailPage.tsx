import React from 'react'
import ContentDetailPage from './ContentDetailPage'
import { faqDetailPageConfig } from './contentPageConfigs'

const FaqDetailPage: React.FC = () => {
  return <ContentDetailPage config={faqDetailPageConfig} />
}

export default FaqDetailPage
