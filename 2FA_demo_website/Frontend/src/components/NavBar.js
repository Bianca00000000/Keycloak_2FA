import { Button, Navbar, Nav} from 'react-bootstrap';
import { BsPersonCircle } from 'react-icons/bs';
import { FaHome } from 'react-icons/fa';
import { useEffect, useState } from 'react';

function NavBar() {

  const [color, setColor] = useState('#fff');

  useEffect(() => {
    const intervalId = setInterval(() => {
      setColor(color => color === '#fff' ? '#228B22' : '#fff');
    }, 1000);

    return () => clearInterval(intervalId);
  }, []);


  return (
    <Navbar bg="dark" variant="dark" expand="lg" sticky="top">
        <Navbar.Brand href="#home" className="d-flex align-items-center">
          <FaHome className="me-2" size='1.5rem'/>
          <span style={{ color: color, transition: 'color 1s' }}>
            Bianca, it's time to vote!
          </span>
        </Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="me-auto">
          </Nav>
          <Nav>
            <Button variant="outline-success" href="/profile" className="d-flex align-items-center">
              <BsPersonCircle className="me-2" size='1.5rem' /> View Profile
            </Button>
          </Nav>
        </Navbar.Collapse>
    </Navbar>
  );
}

export default NavBar;