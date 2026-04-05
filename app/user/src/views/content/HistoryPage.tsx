import React from 'react'
import SingletonContentPage from './SingletonContentPage'
import { singletonContentPageConfigs } from './contentPageConfigs'

const HistoryPage: React.FC = () => {
  return <SingletonContentPage config={singletonContentPageConfigs.history} />
}

export default HistoryPage
