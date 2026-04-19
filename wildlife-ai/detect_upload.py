import cv2
import requests

# Replace with your backend API
API_URL = "http://localhost:8081/api/detect/upload"

# Initialize camera
cap = cv2.VideoCapture(0)

while True:
    ret, frame = cap.read()
    if not ret:
        break

    # TODO: Run YOLO detection here and get result
    # For demo, let's assume we detected a "tiger"
    animal = "tiger"
    category = "dangerous"
    confidence = 0.95

    # Save frame temporarily
    filename = "frame.jpg"
    cv2.imwrite(filename, frame)

    # Upload to backend
    files = {'image': open(filename, 'rb')}
    data = {'animal': animal, 'category': category, 'confidence': confidence}
    response = requests.post(API_URL, files=files, data=data)

    cv2.imshow("AI Detection", frame)
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

cap.release()
cv2.destroyAllWindows()