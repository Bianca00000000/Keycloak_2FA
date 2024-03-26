import React from 'react';
import { Container, Row, Card, Button } from 'react-bootstrap';
import NavBar from '../components/NavBar';
import '../CSS/CandidateCard.css'
import CandidateCard  from '../components/CandidateCard';

// to be modified
import candidate1Image from '../candidat1.jpg';
import candidate2Image from '../candidat2.jpg';
import candidate3Image from '../candidat3.jpg';

const candidates = [
  { id: 1, name: 'Will Williams', slogan: 'For the many, not the few', color: '#6c5ce7', imageUrl: candidate1Image },
  { id: 2, name: 'Jeremy Marte', slogan: 'Black Lives Matter', color: '#00b894', imageUrl: candidate2Image },
  { id: 3, name: 'Timothy Foster', slogan: "Don't just hope for a better life, vote for it!", color: '#d63031', imageUrl: candidate3Image }
];

function User() {

  const onHoverStyle = {
    transform: 'scale(1.05)'
  };

  return (
    <div className="min-vh-100 d-flex flex-column">
      <NavBar />
      <Container className="my-auto">
        <Row>
          {candidates.map(candidate => (
            <CandidateCard key={candidate.id} candidate={candidate} onHoverStyle={onHoverStyle}>
              <Card.Title className={`fw-bold`}>{candidate.name}</Card.Title>
              <Card.Text className="text-secondary">{candidate.slogan}</Card.Text>
              <Button variant="primary" className="mt-auto">Vote</Button>
            </CandidateCard>
          ))}
        </Row>
      </Container>
    </div>
  );
}

export default User;

  

