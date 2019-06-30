import React from 'react';
import request from 'request';

class GetConfig extends React.Component{
    constructor(props){
        super(props);
        this.state = {
            config: "placeholder"
        };
        this.getConfig = this.getConfig.bind(this);
    }

    getConfig() {
        fetch('http://127.0.0.1:8090/config')
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
        let query = "?testconfig=" + this.state.config;
        request.put("http://127.0.0.1:8090/config" + query);
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
