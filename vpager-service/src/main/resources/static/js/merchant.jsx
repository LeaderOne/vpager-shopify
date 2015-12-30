var parseQueryString = function () {

    var str = document.location.search;
    var objURL = {};

    str.replace(
        new RegExp("([^?=&]+)(=([^&]*))?", "g"),
        function ($0, $1, $2, $3) {
            objURL[$1] = $3;
        }
    );
    return objURL;
};

var params = parseQueryString();

var merchantId = params["merchantId"];

var NumberIndicator = React.createClass({
    render: function () {
        return (
            <h1>Now serving: {this.props.currentNumber}</h1>
        );
    }
});

var ServeCustomerButton = React.createClass({
    render: function() {
        return (
            <button className="takeNextNumber" onClick={this.props.onClick}>
                Serve Next Customer!
            </button>
        );
    }
});

var RewindCustomerButton = React.createClass({
    render: function() {
        return (
            <button className="takeNextNumber" onClick={this.props.onClick}>
                Rewind customer!
            </button>
        );
    }
});

var LineLengthIndicator = React.createClass({
    render: function() {
        return (
            <p>Number of people in line: {this.props.lineLength}</p>
        );
    }
});

var CustomerServiceBox = React.createClass({
    displayName: 'CustomerServiceBox',
    getInitialState: function() {
       return {currentNumber: -1, lineLength: -1};
    },
    render: function() {
        return (
            <div className="customerServiceBox">
                <NumberIndicator currentNumber="0"></NumberIndicator>
                <ServeCustomerButton></ServeCustomerButton>
                <RewindCustomerButton></RewindCustomerButton>
                <LineLengthIndicator lineLength="0"></LineLengthIndicator>
            </div>
        );
    }
});


if(!merchantId) {
    ReactDOM.render(<h1>You don't have a merchant ID.  Did you use the wrong link?</h1>,
        document.getElementById('content'));
} else {
    ReactDOM.render(<CustomerServiceBox></CustomerServiceBox>,
        document.getElementById('content'));
}