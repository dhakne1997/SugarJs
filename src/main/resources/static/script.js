$(document).ready(function() {
    $('#getSugarDataButton').click(function(event) {
        event.preventDefault();
        var fromDate = $('#fromDate').val();
        var toDate = $('#toDate').val();


 if (!fromDate || !toDate) {
        alert('Please enter both from and to dates');
        return;
    }
    
        var sugarDataRequest = {
            fromDate: fromDate,
            toDate: toDate
        };

        $.ajax({
            type: 'POST',
            url: '/api/sugarid',
            contentType: 'application/json',
            data: JSON.stringify(sugarDataRequest),
            success: function(data) {
                var sugarDataGrid = $('#sugarDataGrid');
        sugarDataGrid.empty();
        sugarDataGrid.show(); 
        var table = $('<table border="1" cellpadding="5" cellspacing="0"></table>');
        var tableHeader = '<tr>';
        tableHeader += '<th>Sugar Mill Name</th>';
        tableHeader += '<th>Reserve Price</th>';
        tableHeader += '<th>Season</th>';
        tableHeader += '<th>Warehouse</th>';
        tableHeader += '<th>Auction ID</th>';
        tableHeader += '<th>Total Quantity Offered</th>';
        tableHeader += '<th>H1 Bid Price</th>';
        tableHeader += '<th>H1 Bid Quantity</th>';
        tableHeader += '<th>Quantity Matched</th>';
        tableHeader += '<th>Trade No</th>';
        tableHeader += '<th>Buyer Name</th>';
        tableHeader += '<th>First Round Bid Price</th>';
        tableHeader += '<th>First Round Quantity</th>';
        tableHeader += '<th>First Round Bid Log Time</th>';
        tableHeader += '<th>Quantity Approved To Bidder</th>';
        tableHeader += '<th>No of Bags</th>';
        tableHeader += '<th>Grade</th>';
        tableHeader += '</tr>';
        table.append(tableHeader);

        $.each(data, function(index, sugarData) {
            var tableRow = '<tr>';
            tableRow += '<td>' + sugarData.sugarMillName + '</td>';
            tableRow += '<td>' + sugarData.reservePrice + '</td>';
            tableRow += '<td>' + sugarData.season + '</td>';
            tableRow += '<td>' + sugarData.warehouse + '</td>';
            tableRow += '<td>' + sugarData.auctionId + '</td>';
            tableRow += '<td>' + sugarData.totalQuantityOffered + '</td>';
            tableRow += '<td>' + sugarData.h1BidPrice + '</td>';
            tableRow += '<td>' + sugarData.h1BidQuantity + '</td>';
            tableRow += '<td>' + sugarData.quantityMatched + '</td>';
            tableRow += '<td>' + sugarData.tradeNo + '</td>';
            tableRow += '<td>' + sugarData.buyerName + '</td>';
            tableRow += '<td>' + sugarData.firstRoundBidPrice + '</td>';
            tableRow += '<td>' + sugarData.firstRoundQuantity + '</td>';
            tableRow += '<td>' + sugarData.firstRoundBidLogTime + '</td>';
            tableRow += '<td>' + sugarData.quantityApprovedToBidder + '</td>';
            tableRow += '<td>' + sugarData.noOfBags + '</td>';
            tableRow += '<td>' + sugarData.grade + '</td>';
            tableRow += '</tr>';
            table.append(tableRow);
        });
        sugarDataGrid.append(table);
            },
            error: function(xhr, status, error) {
                console.log('Error: ' + error);
            }
        });
    });
    
    
    //download
    
   $('#download').click(function(event) {
    event.preventDefault();
    var fromDate = $('#fromDate').val();
    var toDate = $('#toDate').val();

    if (!fromDate || !toDate) {
        alert('Please enter both from and to dates');
        return;
    }

    var sugarDataRequest = {
        fromDate: fromDate,
        toDate: toDate
    };

    $.ajax({
        type: 'POST',
        url: '/Ncdfipdf/generatePdf',
        contentType: 'application/json',
        data: JSON.stringify(sugarDataRequest),
        xhrFields: {
            responseType: 'blob'
        },
        success: function(data) {
            var blob = new Blob([data], {type: 'application/pdf'});
            var link = document.createElement('a');
            link.href = URL.createObjectURL(blob);
            link.download = 'Sugar_Auction_info.pdf';
            link.click();
        }
    });
});
});