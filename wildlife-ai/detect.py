from flask import Flask, Response
import cv2
from ultralytics import YOLO
import time
import requests
import os
import threading

app = Flask(__name__)

model = YOLO("yolov8n.pt")

cap = cv2.VideoCapture(0)
if not cap.isOpened():
    print("Error: Could not open camera")
    exit(1)

API_URL = "http://localhost:8082/api/detect/upload"
dangerous_animals = ["tiger", "lion", "bear", "elephant"]
last_sent_time = 0
cooldown = 5
last_detected_label = ""

def send_to_api(filename, data):
    try:
        with open(filename, 'rb') as img:
            files = {'image': img}
            requests.post(API_URL, files=files, data=data)
    except:
        pass
    finally:
        if os.path.exists(filename):
            os.remove(filename)

def generate_frames():
    global last_sent_time, last_detected_label

    while True:
        ret, frame = cap.read()
        if not ret:
            continue

        debug_frame = frame.copy()

        results = model(frame)

        for r in results:
            for box in r.boxes:
                cls = int(box.cls[0])
                confidence = float(box.conf[0])
                label = model.names[cls]

                current_time = time.time()

                if label in dangerous_animals and (label != last_detected_label and current_time - last_sent_time > cooldown):

                    filename = f"{label}_{int(current_time)}.jpg"
                    cv2.imwrite(filename, frame)

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

                    threading.Thread(target=send_to_api, args=(filename, data)).start()

                    last_detected_label = label
                    last_sent_time = current_time

                x1, y1, x2, y2 = map(int, box.xyxy[0])
                cv2.rectangle(debug_frame, (x1, y1), (x2, y2), (0, 0, 255), 2)
                cv2.putText(debug_frame, label, (x1, y1 - 10),
                            cv2.FONT_HERSHEY_SIMPLEX, 0.6, (0, 0, 255), 2)

        cv2.imshow("YOLO Detection", debug_frame)

        if cv2.waitKey(1) & 0xFF == ord('q'):
            break

        ret, buffer = cv2.imencode('.jpg', frame)
        frame_bytes = buffer.tobytes()

        yield (b'--frame\r\n'
               b'Content-Type: image/jpeg\r\n\r\n' + frame_bytes + b'\r\n')

@app.route('/video_feed')
def video_feed():
    return Response(generate_frames(),
                    mimetype='multipart/x-mixed-replace; boundary=frame')

if __name__ == "__main__":
    try:
        app.run(host="0.0.0.0", port=5000, debug=True)
    finally:
        cap.release()
        cv2.destroyAllWindows()