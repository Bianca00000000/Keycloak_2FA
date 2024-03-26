import React from 'react';
import { Container, Row, Card, Col } from 'react-bootstrap';
import '../CSS/VotingResults.css'
import CandidateCard  from '../components/CandidateCard';
import Highcharts from 'highcharts';
import HighchartsReact from 'highcharts-react-official';
import Sidebar from '../components/Sidebar'

import candidate1Image from '../candidat1.jpg';
import candidate2Image from '../candidat2.jpg';
import candidate3Image from '../candidat3.jpg';

const candidates = [
  { id: 1, name: 'Will Williams', slogan: 'For the many, not the few', color: '#6c5ce7', imageUrl: candidate1Image },
  { id: 2, name: 'Jeremy Marte', slogan: 'Black Lives Matter', color: '#00b894', imageUrl: candidate2Image },
  { id: 3, name: 'Timothy Foster', slogan: "Don't just hope for a better life, vote for it!", color: '#d63031', imageUrl: candidate3Image }
];

function VotingResults() {

  const options = {
    chart: {
      type: 'bar'
    },
    title: {
      text: 'Rezultatele Candidatilor'
    },
    xAxis: {
      categories: candidates.map(candidate => candidate.name)
    },
    yAxis: {
      min: 0,
      title: {
        text: 'Voturi'
      }
    },
    series: [{
      name: 'Voturi/Candidate',
      data: candidates.map((candidate, index) => {
        return {
          y: [50, 70, 40][index],
          color: candidate.color
        };
      })
    }]
  };

  const onHoverStyle = {
    transform: 'scale(1.05)'
  };

  return (
    <div className='admin-page'>
      <Sidebar />
        <main className='content'>
        <h1>Results</h1>
          <Container className="my-auto">
            <Row>
              {candidates.map(candidate => (
                <Col xs={12} sm={6} md={4} className="mb-4">
                  <CandidateCard key={candidate.id} candidate={candidate} onHoverStyle={onHoverStyle} className="voting-results-card">
                    <Card.Title className={`fw-bold`}>{candidate.name}</Card.Title>
                  </CandidateCard>
                </Col>
              ))}
            </Row>
          </Container>
          <div style={{ maxWidth: '800px', margin: '70px auto' }}>
            <HighchartsReact highcharts={Highcharts} options={options} />
          </div>
        </main>
    </div>
  );
};

export default VotingResults;