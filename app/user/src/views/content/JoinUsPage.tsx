import React from 'react'
import SingletonContentPage from './SingletonContentPage'
import { singletonContentPageConfigs } from './contentPageConfigs'

const JoinUsPage: React.FC = () => {
  return <SingletonContentPage config={singletonContentPageConfigs.joinUs} />
}

export default JoinUsPage
