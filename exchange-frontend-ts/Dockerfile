# Stage 1: Build the React Application
FROM node:26-alpine AS builder

WORKDIR /app

# Copy package files first to leverage Docker layer caching
COPY package*.json ./

# Install dependencies cleanly (omit devDeps if possible, or use npm ci)
RUN npm ci

# Copy the rest of your application code
COPY . .

# Build the app (Generates 'dist/' for Vite or 'build/' for Create React App)
RUN npm run build

# Stage 2: Serve the production assets
FROM nginx:alpine AS runtime

# Clean up default NGINX public files
RUN rm -rf /usr/share/nginx/html/*

# Copy production assets from the builder stage 
# NOTE: If using Create React App, change "/app/dist" to "/app/build"
COPY --from=builder /app/dist /usr/share/nginx/html

# Expose port 80 for the NGINX web server
EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]