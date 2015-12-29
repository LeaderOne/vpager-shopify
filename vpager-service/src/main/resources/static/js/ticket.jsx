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
var ticketId = params["ticketId"];

var numberUrl = "/ticket/" + merchantId + "/" + ticketId;

var stompClient = null;

var NumberIndicator = React.createClass({
    getInitialState: function () {
        return {currentNumber: 0};
    },
    render: function () {
        return (
            <h1>My number is: {this.props.currentNumber}</h1>
        );
    }
});

var TakeNumberButton = React.createClass({
    render: function () {
        return (
            <button className="takeNextNumber">
                Hello world, I am a button!
            </button>
        );
    }
});

var NumberBox = React.createClass({
    displayName: 'NumberBox',
    getInitialState: function () {
        return {currentNumber: -1};
    },
    getCurrentNumber: function () {
        $.ajax({
            url: this.props.url,
            dataType: 'text',
            cache: false,
            success: function (data) {
                this.setState({currentNumber: data});
            }.bind(this),
            error: function (xhr, status, err) {
                console.error(this.props.url, status, err.toString());
            }.bind(this)
        });
    },
    connectToService: function () {
        var socket = new SockJS('/nowserving');
        var self = this;

        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            console.log('Connected: ' + frame);
            stompClient.subscribe('/topic/nowserving/' + merchantId, function (nowServing) {
                self.getCurrentNumber();
            });
        });
    },
    componentDidMount: function () {
        this.getCurrentNumber();
        this.connectToService();
    },
    render: function () {
        return (
            <div className="numberBox">
                <NumberIndicator currentNumber={this.state.currentNumber}></NumberIndicator>
                <TakeNumberButton></TakeNumberButton>
            </div>
        );
    }
});

ReactDOM.render(
    <NumberBox url={numberUrl}></NumberBox>,
    document.getElementById('content')
);