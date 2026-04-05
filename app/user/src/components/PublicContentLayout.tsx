import React, { useEffect } from 'react'
import { Breadcrumb } from 'antd'
import { HomeOutlined } from '@ant-design/icons'
import { Link, useLocation } from 'react-router-dom'
import PortalHeader from '@components/PortalHeader'
import PortalFooter from '@components/PortalFooter'
import './PublicContentLayout.scss'

interface PublicContentLayoutProps {
  section: string
  title: string
  description: string
  children: React.ReactNode
}

const PublicContentLayout: React.FC<PublicContentLayoutProps> = ({
  section,
  title,
  description,
  children,
}) => {
  const location = useLocation()

  useEffect(() => {
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }, [location.pathname])

  return (
    <div className="public-content-layout">
      <PortalHeader />

      <main className="public-content-layout__main">
        <section className="public-content-layout__hero">
          <div className="container">
            <Breadcrumb
              className="public-content-layout__breadcrumb"
              items={[
                {
                  title: (
                    <Link to="/">
                      <HomeOutlined />
                    </Link>
                  ),
                },
                { title: section },
                { title },
              ]}
            />

            <div className="public-content-layout__hero-content">
              <span className="public-content-layout__badge">{section}</span>
              <h1>{title}</h1>
              <p>{description}</p>
            </div>
          </div>
        </section>

        <section className="public-content-layout__body">
          <div className="container">{children}</div>
        </section>
      </main>

      <PortalFooter />
    </div>
  )
}

export default PublicContentLayout
