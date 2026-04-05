import React from 'react'
import SingletonContentPage from './SingletonContentPage'
import { singletonContentPageConfigs } from './contentPageConfigs'

const MediaPage: React.FC = () => {
  return <SingletonContentPage config={singletonContentPageConfigs.media} />
}

export default MediaPage
