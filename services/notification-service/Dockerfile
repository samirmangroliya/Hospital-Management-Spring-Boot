# Stage 1: Build TypeScript
FROM node:20-alpine AS builder
WORKDIR /app

# Copy configuration files from the subfolder path
COPY services/notification-service/package*.json services/notification-service/tsconfig.json ./
RUN npm install

# Copy source code from the subfolder path
COPY services/notification-service/src ./src
RUN npm run build

# Stage 2: Production runtime
FROM node:20-alpine
WORKDIR /app

# Copy production runtime requirements
COPY services/notification-service/package*.json ./
RUN npm install --only=production

# Pull compiled javascript from step 1
COPY --from=builder /app/dist ./dist

EXPOSE 8085
ENV NODE_ENV=production
CMD ["node", "dist/app.js"]
