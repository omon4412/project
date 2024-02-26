# этап сборки (build stage)
FROM node:lts-alpine as build-stage
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# этап production (production-stage)
FROM nginx:stable-alpine as production-stage
# Копируем собранные файлы Vue.js из предыдущего этапа
COPY --from=build-stage /app/dist /usr/share/nginx/html
# Копируем файл конфигурации Nginx
COPY nginx.conf /etc/nginx/nginx.conf
# Указываем порт, на котором будет работать Nginx
EXPOSE 80
# Запускаем Nginx в фоновом режиме
CMD ["nginx", "-g", "daemon off;"]
