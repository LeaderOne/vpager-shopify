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
var stompClient = null;

var merchantId = params["merchantId"];

var nowServingSocket = "/nowserving";
var nowServingTopic = "/topic/nowserving/" + merchantId;

var serveUrl = "/merchant/" + merchantId + "/serve";
var rewindUrl = "/merchant/" + merchantId + "/rewind";
var lineLenUrl = "/merchant/" + merchantId + "/lineLength";
var nowServingUrl = "/merchant/" + merchantId;

var barcodeSrc = "/merchant/" + merchantId + "/hangoutashingle";
var ticketHref = "/ticket.html?merchantId=" + merchantId;

var NumberIndicator = React.createClass({
    render: function () {
        return (
            <h1>Now Serving Ticket ID: <u>{this.props.nowServingCustomer}</u></h1>
        );
    }
});

var ServeCustomerButton = React.createClass({
    render: function() {
        return (
            <button className="serveCustomer" onClick={this.props.onClick}>
                Serve Next Customer!
            </button>
        );
    }
});

var RewindCustomerButton = React.createClass({
    render: function() {
        return (
            <button className="rewindCustomer" onClick={this.props.onClick}>
                Rewind customer!
            </button>
        );
    }
});

var LineLengthIndicator = React.createClass({
    render: function() {
        return (
            <p>There are <u><b>{this.props.lineLength}</b></u> people in line.</p>
        );
    }
});

var BarcodeImage = React.createClass({
    render: function() {
        return (
            <a href={ticketHref}>
            <img src={barcodeSrc}></img>
            </a>
        );
    }
});

var BarcodeLink = React.createClass({
    render: function() {
        return (
            <p>Your link is:<br/>
            <a href={ticketHref}>{ticketHref}</a>
            </p>
        );
    }
});

var CustomerServiceBox = React.createClass({
    displayName: 'CustomerServiceBox',
    getInitialState: function() {
       return {nowServingCustomer: -1, lineLength: -1};
    },
    connectToService: function () {
        var socket = new SockJS(this.props.nowServingSocket);
        var self = this;

        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            console.log('Connected: ' + frame);
            stompClient.subscribe(self.props.nowServingTopic, function (nowServingCustomer) {
                console.log("Setting state " + nowServingCustomer.body);
                self.setState(JSON.parse(nowServingCustomer.body));
            });
        });
    },
    getNumbersForInitState: function() {
        console.log("In getNumbersForInitState...");

        $.ajax({
            url: this.props.nowServingUrl,
            dataType: 'text',
            cache: false,
            success: function (data) {
                console.log("Received response from " + this.props.nowServingUrl + ": " + data);
                this.setState({nowServingCustomer: data});
            }.bind(this),
            error: function (xhr, status, err) {
                console.error(this.props.nowServingUrl, status, err.toString());
            }.bind(this)
        });

        $.ajax({
            url: this.props.lineLenUrl,
            dataType: 'text',
            cache: false,
            success: function (data) {
                console.log("Received response from " + this.props.lineLenUrl + ": " + data);
                this.setState({lineLength: data});
            }.bind(this),
            error: function (xhr, status, err) {
                console.error(this.props.nowServingUrl, status, err.toString());
            }.bind(this)
        });
    },
    componentDidMount: function() {
        this.getNumbersForInitState();
        this.connectToService();
    },
    serveCustomer: function() {
        $.ajax({
            type: 'POST',
            url: this.props.serveUrl,
            dataType: 'text',
            cache: false,
            success: function (data) {
                console.log("Received response from serve " + data);
            }.bind(this),
            error: function (xhr, status, err) {
                console.error(this.props.serveUrl, status, err.toString());
            }.bind(this)
        });
    },
    rewindCustomer: function() {
        $.ajax({
            type: 'POST',
            url: this.props.rewindUrl,
            dataType: 'text',
            cache: false,
            success: function (data) {
                console.log("Received response from rewind ticket " + data);
            }.bind(this),
            error: function (xhr, status, err) {
                console.error(this.props.rewindUrl, status, err.toString());
            }.bind(this)
        });
    },
    render: function() {
        return (
            <div className="customerServiceBox">
                <NumberIndicator nowServingCustomer={this.state.nowServingCustomer}></NumberIndicator>
                <ServeCustomerButton onClick={this.serveCustomer}></ServeCustomerButton>
                <br />
                <br />
                <RewindCustomerButton onClick={this.rewindCustomer}></RewindCustomerButton>
                <LineLengthIndicator lineLength={this.state.lineLength}></LineLengthIndicator>

                <p>Your customers can scan the QR code below to take a ticket:</p>
                <BarcodeImage></BarcodeImage>

                <p>Or, if you aren't comfortable with a QR code, you can copy this link:</p>
                <BarcodeLink></BarcodeLink>
            </div>
        );
    }
});


if(!merchantId) {
    ReactDOM.render(<h1>You don't have a merchant ID.  Did you use the wrong link?</h1>,
        document.getElementById('content'));
} else {
    ReactDOM.render(<CustomerServiceBox nowServingSocket={nowServingSocket}
                                        nowServingTopic={nowServingTopic}
                                        lineLenUrl={lineLenUrl}
                                        nowServingUrl={nowServingUrl}
                                        serveUrl={serveUrl}
                                        rewindUrl={rewindUrl}></CustomerServiceBox>,
        document.getElementById('content'));
}