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

function checkNotifications() {
    if(!("Notification" in window)) {
        alert("This browser doesn't support notifications.  You'll need to watch the page for your number.");
    } else if(Notification.permission === "granted") {

    } else if(Notification.permission !== 'denied') {
        Notification.requestPermission(function(permission) {
            if(permission === "denied") {
                alert("You have turned off notifications.  You'll need to watch the page for your number.");
            }
        });
    }
}

var params = parseQueryString();

var merchantId = params["merchantId"];
var ticketId = params["ticketId"];

var numberUrl = "/ticket/" + merchantId + "/" + ticketId;
var takeUrl = "/ticket/" + merchantId;
var nowServingSocket = "/nowserving";
var nowServingTopic = "/topic/nowserving/" + merchantId;

var stompClient = null;

var NumberIndicator = React.createClass({
    getInitialState: function () {
        return {currentNumber: 0};
    },
    render: function () {
        return (
            <div>
            <h1>My number is: {this.props.currentNumber}</h1>
            <p>My ticket ID is: {ticketId}</p>
            </div>
        );
    }
});

var TakeNumberButton = React.createClass({
    render: function () {
        return (
            <button className="takeNextNumber" onClick={this.props.onClick}>
                Take Next Number!
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
        var socket = new SockJS(this.props.nowServingSocket);
        var self = this;

        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            console.log('Connected: ' + frame);
            stompClient.subscribe(self.props.nowServingTopic, function (nowServing) {
                self.getCurrentNumber();

                var nowServingId = JSON.parse(nowServing.body);
                var options = {body: "You are first in line!", sound: "sound/alert.wav"};

                if(nowServingId.nowServingCustomer == ticketId) {
                    var notification = new Notification("Your order is ready!", options);
                }
            });
        });
    },
    componentDidMount: function () {
        this.getCurrentNumber();
        this.connectToService();
        checkNotifications();
    },
    getNewNumber: function() {
        console.log("In get new number");

        $.ajax({
            type: 'PUT',
            url: this.props.takeUrl,
            dataType: 'text',
            cache: false,
            success: function (data) {
                console.log("Received response from new ticket " + data);
                var newTicket = JSON.parse(data);
                var newTicketId = newTicket.id;

                window.location.href = "/ticket.html?merchantId=" + merchantId + "&ticketId=" + newTicketId;
            }.bind(this),
            error: function (xhr, status, err) {
                console.error(this.props.takeUrl, status, err.toString());
            }.bind(this)
        });
    },
    render: function () {
        return (
            <div className="numberBox">
                <NumberIndicator currentNumber={this.state.currentNumber}></NumberIndicator>
                <TakeNumberButton onClick={this.getNewNumber}></TakeNumberButton>
            </div>
        );
    }
});

if(!merchantId) {
    ReactDOM.render(
        <h1>You don't have a merchant selected.  Did you scan the wrong QR code?</h1>,
        document.getElementById('content')
    );
} else if(!ticketId) {
    function getFirstNumber() {
        console.log("In get first number");

        $.ajax({
            type: 'PUT',
            url: "/ticket/" + merchantId,
            dataType: 'text',
            cache: false,
            success: function (data) {
                console.log("Received response from new ticket " + data);
                var newTicket = JSON.parse(data);
                var newTicketId = newTicket.id;

                window.location.href = "/ticket.html?merchantId=" + merchantId + "&ticketId=" + newTicketId;
            }.bind(this),
            error: function (xhr, status, err) {
                console.error("/ticket/" + merchantId, status, err.toString());
            }.bind(this)
        });
    }

    ReactDOM.render(
        <TakeNumberButton onClick={getFirstNumber} ></TakeNumberButton>,
        document.getElementById('content')
    );
} else {
    ReactDOM.render(
        <NumberBox url={numberUrl} takeUrl={takeUrl} nowServingTopic={nowServingTopic} nowServingSocket={nowServingSocket}></NumberBox>,
        document.getElementById('content')
    );
}