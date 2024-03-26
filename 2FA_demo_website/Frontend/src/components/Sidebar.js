import React from 'react';
import { Nav } from 'react-bootstrap';
import { BsFillGrid3X3GapFill } from 'react-icons/bs';
import '../CSS/Sidebar.css'

// De modificat
import profileImage from '../profile_admin.jpg'

function Sidebar() {
    // de modificat
    const userName = "Bianca Daniela";
    const userStatus = "Online";
    
    return (
        <Nav  className='sidebar-style flex-column' defaultActiveKey="/admin">
        <div className='profile-style'>
            <img
            src={profileImage}
            alt="Profile"
            className='image-style'
            />
            <h5 className='name-style'>{userName}</h5>
            <div className='status-style'>
            <span className='status-indicator-style'></span>
            {userStatus}
            </div>
        </div>
        <Nav.Link href="/admin" className='link-style'>
            <BsFillGrid3X3GapFill className='icon-style' />
            Overview
        </Nav.Link>
        <Nav.Item className='section-title-style'>REPORTS</Nav.Item>
        <Nav.Link href="/candidates" className='link-style'>
            <BsFillGrid3X3GapFill className='icon-style' />
            Candidates
        </Nav.Link>
        <Nav.Link href="/results" className='link-style'>
            <BsFillGrid3X3GapFill className='icon-style' />
            Results
        </Nav.Link>
        <Nav.Link href="/voters" className='link-style'>
            <BsFillGrid3X3GapFill className='icon-style' />
            Voters
        </Nav.Link>
        <Nav.Link href="/position" className='link-style'>
            <BsFillGrid3X3GapFill className='icon-style' />
            Position
        </Nav.Link>
        <Nav.Item className='section-title-style'>Settings</Nav.Item>
        <Nav.Link href="/title-election" className='link-style'>
            <BsFillGrid3X3GapFill className='icon-style' />
            Election Title
        </Nav.Link>
        <Nav.Link href="/view-profile" className='link-style'>
            <BsFillGrid3X3GapFill className='icon-style' />
            View Profile
        </Nav.Link>
        </Nav>
    );
}

export default Sidebar;