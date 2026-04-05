import React from 'react'
import SingletonContentPage from './SingletonContentPage'
import { singletonContentPageConfigs } from './contentPageConfigs'

const CompanyPage: React.FC = () => {
  return <SingletonContentPage config={singletonContentPageConfigs.company} />
}

export default CompanyPage
