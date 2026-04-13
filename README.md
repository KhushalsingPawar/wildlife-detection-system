# 🐾 WildGuard AI – Real-Time Wildlife Detection System

## 📌 Overview
WildGuard AI is an AI-powered real-time wildlife detection system designed to identify animal intrusion from live video streams and generate instant alerts. The system helps improve safety and monitoring in forest areas and rural zones.

## 🚀 Key Features
- Real-time wildlife detection using YOLOv8 and OpenCV  
- Live video stream processing with instant alert generation  
- SMS notifications using Twilio API  
- Secure authentication using JWT and RBAC  
- Cloud-based image storage using AWS S3  
- Interactive dashboard for monitoring alerts and activity  

## 🛠️ Tech Stack
- **Frontend:** React.js  
- **Backend:** Spring Boot (REST APIs)  
- **Database:** MySQL  
- **AI Model:** YOLOv8, OpenCV  
- **Cloud:** AWS S3  
- **Authentication:** JWT, Role-Based Access Control  
- **Notifications:** Twilio API  

## ⚙️ System Workflow
1. Capture live video stream from camera  
2. Process frames using YOLOv8 for object detection  
3. Identify wildlife intrusion events  
4. Store captured images in AWS S3  
5. Send real-time SMS alerts to users  


## ▶️ Installation & Setup

### Backend
```bash
cd backend
mvn spring-boot:run

### Frontend
```bash
cd frontend
npm install
npm start
