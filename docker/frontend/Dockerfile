FROM node:18
WORKDIR /usr/src/app
COPY . . 
RUN ls -la
RUN npm install
EXPOSE 8081
# ENTRYPOINT [ "npm run dev" ]
CMD [ "npm", "run", "dev" ]