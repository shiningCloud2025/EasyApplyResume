import React from 'react'
import ContentListPage from './ContentListPage'
import { faqListPageConfig } from './contentPageConfigs'

const FaqListPage: React.FC = () => {
  return <ContentListPage config={faqListPageConfig} />
}

export default FaqListPage
