export const apiEndpoint = `http://a2cbb9cea6b0541e1b2d9f59e5ab7e36-1453076124.eu-central-1.elb.amazonaws.com:8080` // backend url
// export const apiEndpoint = `http://localhost:8080`

export const authConfig = {
  domain: 'dev-b5l8pxg7z3snujdy.us.auth0.com',            // Auth0 domain
  clientId: 'K0KPyXXrmwDLRNiIrBulZYq93NROLedq',          // Auth0 client id
  callbackUrl: 'http://a932eac94ad504c3fac624609a6db908-318301360.eu-central-1.elb.amazonaws.com/callback' // frontend url
  // callbackUrl: 'http://localhost:3000/callback'
}
