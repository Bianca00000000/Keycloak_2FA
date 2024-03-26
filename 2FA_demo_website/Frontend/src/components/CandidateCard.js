import React from 'react';
import { Card, Col } from 'react-bootstrap';

const CandidateCard = ({ candidate, onHoverStyle, children }) => (
  <Col md={4} className="mb-4">
    <Card
      className="text-center shadow card-style"
      onMouseEnter={(e) => e.currentTarget.style.transform = onHoverStyle.transform}
      onMouseLeave={(e) => e.currentTarget.style.transform = 'none'}
    >
      <Card.Header className="card-header-style" style={{ backgroundColor: candidate.color }} />
      <Card.Img variant="top" src={candidate.imageUrl} className="card-image-style" />
      {children && <Card.Body className="d-flex flex-column">{children}</Card.Body>}
    </Card>
  </Col>
);

export default CandidateCard;