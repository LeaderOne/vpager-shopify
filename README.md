# VPager

VPager was a proof of concept of a virtual "take a number" system.  The idea is that rather than 
standing in line, customers could scan a QR code or visit a website to take a virtual "ticket" 
and would then be notified via Web Notification API that they were ready to be served.  
Restaurants normally use "pagers" for this purpose, but this would have allowed a user to use a
standard smart phone.

There are several versions of this project that I built over the years.  This one uses the Shopify
API, the idea being that eventually we could integrate with Shopify's POS functionality.  The
project include services which authenticate a user as well as validating a Shopify request.  Users
could then log in using OAuth2 tokens from Shopify.

Unfortunately, Shopify never provided an easy way to validate a customer, so this functionality
was never completed.

## Project Structure

The project is divided into vpager-service, vpager-shopify, vpagerios, and vpager-db.  
vpager-db contains a Docker container that can be used to set up the VPager database for testing.
vpager-shopify (which is empty) was the source for the front-end liquid templates and resources.
vpager-ios (generated project) was the mobile application.
