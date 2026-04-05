import React from 'react'
import SingletonContentPage from './SingletonContentPage'
import { singletonContentPageConfigs } from './contentPageConfigs'

const ContactServicePage: React.FC = () => {
  return <SingletonContentPage config={singletonContentPageConfigs.contact} />
}

export default ContactServicePage
