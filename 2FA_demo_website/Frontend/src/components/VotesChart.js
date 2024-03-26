import React from 'react';
import Highcharts from 'highcharts';
import HighchartsReact from 'highcharts-react-official';

const VotesChart = () => {
  // to be modified
  const data = Array.from({ length: 24 }, () => Math.floor(Math.random() * 100));

  const options = {
    chart: {
      type: 'spline', 
      backgroundColor: '#f0f0f0',
      style: {
        fontFamily: 'Arial',
        fontWeight: 'bold'
      }
    },
    title: {
        text: 'The number of votes / hour',
        align: 'center',
        style: {
          color: '#333333',
          fontWeight: 'bold'
        }
    },
    xAxis: {
      categories: [...Array(24).keys()].map(hour => `${hour}:00`),
      labels: {
        style: {
          color: '#333333'
        }
      }
    },
    yAxis: {
      title: {
        text: 'Votes/Hour',
        style: {
          color: '#333333'
        }
      }
    },
    plotOptions: {
      series: {
        marker: {
          fillColor: '#FFFFFF',
          lineWidth: 2,
          lineColor: null
        }
      }
    },
    series: [{
      name: 'Votes',
      data: data,
      color: '#007bff'
    }]
  };

  return (
    <div style={{ marginTop: '30px' }}>
      <HighchartsReact highcharts={Highcharts} options={options} />
    </div>
  );
};

export default VotesChart;