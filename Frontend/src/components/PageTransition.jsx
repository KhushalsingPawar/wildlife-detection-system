import { motion } from 'framer-motion'

const MotionDiv = motion.div

export function PageTransition({ children, className = '' }) {
  return (
    <MotionDiv
      className={className}
      initial={false}
      animate={{ opacity: 1, y: 0 }}
      exit={{ opacity: 0, y: -6 }}
      transition={{ duration: 0.25, ease: [0.22, 1, 0.36, 1] }}
    >
      {children}
    </MotionDiv>
  )
}
