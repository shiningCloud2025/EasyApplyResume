import React from 'react'
import SingletonContentPage from './SingletonContentPage'
import { singletonContentPageConfigs } from './contentPageConfigs'

const PartnersPage: React.FC = () => {
  return <SingletonContentPage config={singletonContentPageConfigs.partners} />
}

export default PartnersPage
