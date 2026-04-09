import cv2
import requests
from ultralytics import YOLO
import time
import os

print("🚀 Starting AI Detection System...")

model = YOLO("yolov8n.pt")
cap = cv2.VideoCapture(0)

if not cap.isOpened():
    print("❌ Camera not opened")
    exit()

print("📷 Camera started")

# ✅ FIX 1: Correct PORT (8082)
API_URL = "http://localhost:8082/api/detect/upload"

dangerous_animals = ["tiger", "lion", "bear", "elephant"]
last_sent_time = 0
cooldown = 5
last_detected_label = ""

while True:
    ret, frame = cap.read()
    if not ret:
        print("❌ Frame error")
        break

    results = model(frame)

    for r in results:
        for box in r.boxes:
            cls = int(box.cls[0])
            confidence = float(box.conf[0])
            label = model.names[cls]

            x1, y1, x2, y2 = map(int, box.xyxy[0])
            cv2.rectangle(frame, (x1, y1), (x2, y2), (0, 0, 255), 2)
            cv2.putText(frame, label, (x1, y1 - 10),
                        cv2.FONT_HERSHEY_SIMPLEX, 0.6, (0, 0, 255), 2)

            # ✅ Only check dangerous animals
            if label in dangerous_animals:
                current_time = time.time()

                if (label != last_detected_label and current_time - last_sent_time > cooldown):
                    print(f"🚨 DANGEROUS DETECTED: {label} | {confidence:.2f}")

                    # Save frame temporarily
                    filename = f"{label}_{int(time.time())}.jpg"
                    cv2.imwrite(filename, frame)

                    # ✅ FIX 2: Add latitude & longitude
                    data = {
                        "animalName": label,
                        "category": "DANGEROUS",
                        "confidence": confidence,
                        "location": "Forest Zone A",
                        "cameraId": "CAM-01",
                        "detectedBy": "AI",
                        "latitude": 12.34,
                        "longitude": 56.78
                    }

                    try:
                        # ✅ FIX 3: Proper file handling
                        with open(filename, 'rb') as img:
                            files = {'image': img}
                            response = requests.post(API_URL, files=files, data=data)

                        print("✅ ALERT SENT:", response.status_code)
                        print("📨 Response:", response.text)

                        # Optional: delete file after upload
                        os.remove(filename)

                    except Exception as e:
                        print("❌ API Error:", e)

                    last_detected_label = label
                    last_sent_time = current_time
            else:
                print(f"⚪ Ignored: {label}")

    cv2.imshow("🚨 Wildlife Alert System", frame)

    if cv2.waitKey(1) & 0xFF == ord('q'):
        print("🛑 Detection stopped")
        break

cap.release()
cv2.destroyAllWindows()
print("🔚 System shutdown")