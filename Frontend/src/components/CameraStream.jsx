import React, { useEffect, useRef, useState } from 'react';
import { Camera, RefreshCw } from 'lucide-react';

export function CameraStream() {
  const videoRef = useRef(null);
  const [error, setError] = useState(null);
  const [streamActive, setStreamActive] = useState(false);

  const startCamera = async () => {
    try {
      setError(null);
      const stream = await navigator.mediaDevices.getUserMedia({
        video: { width: 1280, height: 720 },
        audio: false
      });

      if (videoRef.current) {
        videoRef.current.srcObject = stream;
        setStreamActive(true);
      }
    } catch (err) {
      console.error("Camera Error:", err);
      setError("Camera access denied. Please check permissions.");
    }
  };

  useEffect(() => {
    startCamera();
    return () => {
      if (videoRef.current?.srcObject) {
        videoRef.current.srcObject.getTracks().forEach(track => track.stop());
      }
    };
  }, []);

  return (
    <div className="relative aspect-video w-full overflow-hidden rounded-lg bg-slate-900 border border-white/10">
      {error ? (
        <div className="flex h-full flex-col items-center justify-center p-4 text-center">
          <p className="text-xs text-red-400 mb-2">{error}</p>
          <button onClick={startCamera} className="flex items-center gap-2 text-[10px] uppercase bg-white/10 px-3 py-1 rounded hover:bg-white/20">
            <RefreshCw className="size-3" /> Retry
          </button>
        </div>
      ) : (
        <>
          <video 
            ref={videoRef} 
            autoPlay 
            playsInline 
            muted 
            className="h-full w-full object-cover shadow-inner" 
          />
          <div className="absolute top-3 left-3 flex items-center gap-2">
            <span className="flex h-2 w-2 animate-ping rounded-full bg-red-500"></span>
            <span className="font-mono text-[10px] font-bold tracking-tighter text-white drop-shadow-md">
              RAW_FEED_01
            </span>
          </div>
        </>
      )}
    </div>
  );
}