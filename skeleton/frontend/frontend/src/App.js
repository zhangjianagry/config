import React from 'react';
import request from 'request';


var configHost = process.env.SERVICE_SERVICE_HOST;

var configAddress = 'http://' + configHost + ':9080';
class GetConfig extends React.Component{
    constructor(props){
        super(props);
        this.state = {
            config: "placeholder"
        };
        this.getConfig = this.getConfig.bind(this);
    }

    getConfig() {
        console.log(configAddress);
        fetch(configAddress + '/config')
        .then(res => res.json())
        .then(res => this.setState({
            config: res.testconfig
        }));
    }

    render(){
        return (
            <div>
                <span>{ this.state.config }</span>
                <button onClick={this.getConfig}>
                    get
                </button>
            </div>
        )
    }
}

class PutConfig extends React.Component{
    constructor(props){
        super(props);
        this.state = {
            config: "placeholder"
        };
        this.updateConfig = this.updateConfig.bind(this);
        this.putConfig = this.putConfig.bind(this);
    }

    updateConfig(event) {
        this.setState({config: event.target.value})
    }

    putConfig() {

    }

    render(){
        return (
            <div>
                <input type="text" value={this.state.config} onChange={this.updateConfig}></input>
                <button onClick={this.putConfig}>
                    put
                </button>
            </div>
        )
    }
}

function App() {
  return (
    <div className="App">
        <GetConfig></GetConfig>
        <PutConfig></PutConfig>
    </div>
  );
}

export default App;
