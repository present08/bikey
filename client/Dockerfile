FROM node:22
WORKDIR /app
COPY . .
RUN npm install && npm run build
EXPOSE 4000
CMD ["npx", "serve", "-s", "build"]