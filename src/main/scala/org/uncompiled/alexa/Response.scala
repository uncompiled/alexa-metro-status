package org.uncompiled.alexa

import scala.beans.BeanProperty

/* Response class is the HTTP Response
 *
 * @param uid            Unique identifier for each feed item.
 * @param updateDate     Requires Simple Date Format: yyyy-MM-dd'T'HH:mm:ss'.0Z'.
 * @param mainText       The text that is read to the customer.
 * @param redirectionURL Provides the URL target for the Read More link in the Alexa app.
 */
case class Response(@BeanProperty uid: String,
                    @BeanProperty updateDate: String,
                    @BeanProperty titleText: String,
                    @BeanProperty mainText: String,
                    @BeanProperty redirectionURL: String)
