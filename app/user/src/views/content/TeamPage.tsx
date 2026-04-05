import React from 'react'
import SingletonContentPage from './SingletonContentPage'
import { singletonContentPageConfigs } from './contentPageConfigs'

const TeamPage: React.FC = () => {
  return <SingletonContentPage config={singletonContentPageConfigs.team} />
}

export default TeamPage
