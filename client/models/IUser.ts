export interface IUser {
  active: boolean
  username: string
  exp: number
  iat: number
  nbf: number
  sub: string
  iss: string
  email_verified: boolean
  updated_at: string
  roles: any[]
  nickname: string
  name: string
  given_name: string
  nonce: string
  family_name: string
  picture: string
  email: string
  sid: string
  oauth2Provider: string
}
