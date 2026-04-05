import React from 'react'
import ContentListPage from './ContentListPage'
import { guideListPageConfig } from './contentPageConfigs'

const GuideListPage: React.FC = () => {
  return <ContentListPage config={guideListPageConfig} />
}

export default GuideListPage
