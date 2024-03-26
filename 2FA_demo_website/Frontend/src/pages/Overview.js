import React from 'react';
import Sidebar from '../components/Sidebar';
import '../CSS/Overview.css';
import { BsPeople, BsTicket, BsClipboard } from 'react-icons/bs'; 
import VotesChart from '../components/VotesChart';


function Overview() {
  return (
    <div className='admin-page'>
      <Sidebar />
      <main className='content'>
        <h1>Overview</h1>
        <div className='overview-cards'>
          <div className='overview-card'>
            <BsTicket className='card-icon' />
            <div className='card-info'>
              <div className='card-number'>64%</div>
              <div className='card-text'>Participation (1621 voters)</div>
            </div>
          </div>
          <div className='overview-card'>
            <BsPeople className='card-icon' />
            <div className='card-info'>
              <div className='card-number'>3</div>
              <div className='card-text'>Candidates</div>
            </div>
          </div>
          <div className='overview-card'>
            <BsClipboard className='card-icon' />
            <div className='card-info'>
              <div className='card-number'>2</div>
              <div className='card-text'>Positions</div>
            </div>
          </div>
        </div>
        <VotesChart />
      </main>
    </div>
  );
}

export default Overview;