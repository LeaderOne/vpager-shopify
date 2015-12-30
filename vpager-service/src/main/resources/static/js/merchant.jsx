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

var CustomerServiceBox = React.createClass({
    render: function() {
        return (
            <div className="customerServiceBox">
                <NumberIndicator currentNumber="0"></NumberIndicator>
                <ServeCustomerButton></ServeCustomerButton>
                <RewindCustomerButton></RewindCustomerButton>
            </div>
        );
    }
});