// DONE: Once your application is deployed, copy an API id here so that the frontend could interact with it
// export const apiEndpoint = `http://a2a7529a340d94d96b47de57c88c9666-189089577.eu-central-1.elb.amazonaws.com:8080`
export const apiEndpoint = `http://localhost:8080`

export const authConfig = {
  // DONE: Create an Auth0 application and copy values from it into this map. For example:
  // domain: 'dev-nd9990-p4.us.auth0.com',
  domain: 'dev-b5l8pxg7z3snujdy.us.auth0.com',            // Auth0 domain
  clientId: 'K0KPyXXrmwDLRNiIrBulZYq93NROLedq',          // Auth0 client id
  callbackUrl: 'http://localhost:3000/callback'
}
