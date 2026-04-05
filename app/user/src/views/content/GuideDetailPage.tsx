import React from 'react'
import ContentDetailPage from './ContentDetailPage'
import { guideDetailPageConfig } from './contentPageConfigs'

const GuideDetailPage: React.FC = () => {
  return <ContentDetailPage config={guideDetailPageConfig} />
}

export default GuideDetailPage
