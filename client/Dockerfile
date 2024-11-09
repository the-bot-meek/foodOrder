FROM node:alpine AS build
WORKDIR /usr/src/app
COPY . /usr/src/app
RUN npm ci
RUN npm run build --prod

FROM nginx:alpine
WORKDIR /usr/src/app
COPY --from=build /usr/src/app/dist/food-order/browser /usr/share/nginx/html
COPY ./nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
