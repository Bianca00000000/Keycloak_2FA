import React, { useState, useEffect } from 'react';
import { BrowserRouter, Routes, Route, Navigate, useNavigate } from 'react-router-dom';
import Keycloak from 'keycloak-js';
import './App.css';

import Overview from './pages/Overview';
import User from './pages/User';
import VotingResults from './pages/VotingResults';
import Voters from './pages/Voters';

const initOptions = {
  url: 'http://localhost:8080/',
  realm: 'pnrealm',
  clientId: 'DemoVotingApp'
};

const kc = new Keycloak(initOptions);

function App() {
  const [keycloak, setKeycloak] = useState(null);
  const [authenticated, setAuthenticated] = useState(false);

  useEffect(() => {
    kc.init({ onLoad: initOptions.onLoad })
      .then((auth) => {
        if (!auth) {
          setKeycloak(null);
          setAuthenticated(false);
          kc.login(); 
        } else {
          console.info("Authenticated");
          setKeycloak(kc);
          setAuthenticated(true);
        }
      }).catch(err => {
        console.error("Authentication failed", err);
      });
  }, []);

  if (keycloak) {
    if (authenticated) {
      return (
        <BrowserRouter>
          <Routes>
          <Route path="/user" element={<User />} />
          <Route path="/admin" element={<Overview />} />
          <Route path="/results" element={<VotingResults />} />
          <Route path="/voters" element={<Voters />} />
          </Routes>
        </BrowserRouter>
      );
    } else {
      return <div>Please log in.</div>;
    }
  }
  
  return <div>Initializing Keycloak...</div>;
}

export default App;