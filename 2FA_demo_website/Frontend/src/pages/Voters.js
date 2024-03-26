import React from 'react';
import { Container, Row, Col, Table, Button, Image } from 'react-bootstrap';
import Sidebar from '../components/Sidebar'

const VotersList = () => {
  // De luat din baza de date
  const voters = [
    {
      lastname: 'Smith',
      firstname: 'John',
      photo: '../candidat1.jpg', 
      voterId: 'VOT123456'
    },
    {
      lastname: 'Doe',
      firstname: 'Jane',
      photo: '../candidat2.jpg',
      voterId: 'VOT789012'
    },
    {
      lastname: 'Johnson',
      firstname: 'Mike',
      photo: '../candidat3.jpg', 
      voterId: 'VOT345678'
    }
  ];

  return (
    <Container fluid>
      <Row>
        <Col md={2} className="sidebar bg-light">
          <Sidebar />
        </Col>

        <Col md={10}>
          <Row className="mt-3 mb-3">
            <Col>
              <h2>Voters</h2>
            </Col>
            <Col className="text-right">
              <Button variant="primary">+ New</Button>
            </Col>
          </Row>

          <Table striped bordered hover>
            <thead>
              <tr>
                <th>Lastname</th>
                <th>Firstname</th>
                <th>Photo</th>
                <th>Voters ID</th>
                <th>Tools</th>
              </tr>
            </thead>
            <tbody>
              {voters.map((voter, index) => (
                <tr key={index}>
                  <td>{voter.lastname}</td>
                  <td>{voter.firstname}</td>
                  <td><Image src={voter.photo} thumbnail style={{ width: '50px' }} /></td>
                  <td>{voter.voterId}</td>
                  <td>
                    <Button variant="info">Edit</Button>{' '}
                    <Button variant="danger">Delete</Button>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        </Col>
      </Row>
    </Container>
  );
}

export default VotersList;