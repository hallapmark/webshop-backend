import secrets, base64

key = secrets.token_bytes(64)
print(base64.urlsafe_b64encode(key).rstrip(b"=").decode())
# to be used in the backend, e.g. Java/Spring:
# Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(superSecretKey));