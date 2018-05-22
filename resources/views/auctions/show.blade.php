@extends('layouts.base', ['categories' => $categories])

@section('title', $auction->item_name)

@section('resources')
@parent
<link rel="stylesheet" href="{{ asset('css/auction.css') }}">
<link rel="stylesheet" href="{{ asset('css/carousel.css') }}">
<script src="{{ asset('js/auction.js') }}" defer></script>
@endsection

@section('content')
<div class="row mt-5">


    <div class="col-md-9">
        <div class="row">
        </div>
        <div class="row">
            <div class="col-md-12 text-align-center mobile-text-center">

                @include('auctions.components.auction_carousel', ['imageURLs' => $auction->getImagesURLs()])
                <span class="display-4 title">{{ $auction->item_name }}</span>
                <span class="badge badge-primary">Tech</span>
                <span class="badge badge-info">New</span>
                <a href="auction_itemOnWatchList.html" class="far fa-star text-warning " aria-hidden="true" title="Add to Watchlist"></a>
            </div>
        </div><!-- end row-->
    </div>

    {{-- auction owner 'card' --}}
    <div class="col-md-3 ">
        <div class="card text-align-center mobile-text-center" style="width: 20rem;">
            <img class="card-img-top img-fluid" src="https://ir.ebaystatic.com/pictures/aw/social/avatar.png"
                 alt="Auctioneer Image">


            <div class="card-block mb-3">
                <h4 class="card-title"><a href="../profile/profile.html">Auctioneer Name</a></h4>
                <span class="sr-only">Four out of Five Stars</span>
                <span class="fas fa-star" aria-hidden="true"></span>
                <span class="fas fa-star" aria-hidden="true"></span>
                <span class="fas fa-star" aria-hidden="true"></span>
                <span class="fas fa-star" aria-hidden="true"></span>
                <span class="far fa-star" aria-hidden="true"></span>
                <span class="badge badge-success">61</span>
                <p class="card-text">Lorem ipsum dolor sit amet, consectetur adipisicing elit. Amet, consequatur
                    cupiditate dicta doloremque eligendi</p>

                @if(Auth::check())
                @if(Auth::user()->isAuctionOwner($auction))
                <a href="/auctions/{{$auction->id}}/edit" class="btn btn-primary ">Edit Auction</a>
                <a href="#" class="btn btn-danger" data-toggle="modal" data-target="#removeModal">Remove Auction</a>
                {{-- delete auction modal --}}
                <div class="modal fade" id="removeModal" tabindex="-1" role="dialog"
                     aria-labelledby="removeModalLabel" aria-hidden="true">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="removeModalLabel">Remove Auction </h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">

                                <label for="user-pass" class="col-form-label">Password:</label>
                                <input type="password" class="form-control" id="user-pass" name="password">

                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary"
                                        data-dismiss="modal">Close
                                </button>
                                <button id="remove-auction-btn" type="button" class="btn btn-danger report" data-dismiss="modal">Remove</button>
                            </div>
                        </div>
                    </div>
                </div>
                @else
                <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#contactModal"
                        data-whatever="@getbootstrap"> Contact
                </button>
                <div class="modal fade" id="contactModal" tabindex="-1" role="dialog"
                     aria-labelledby="contactModal" aria-hidden="true">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="contactModalLabel">New message</h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">

                                <div class="form-group">
                                    <label for="recipient-name" class="form-control-label">To:</label>
                                    <input type="text" class="form-control" id="dest-name">
                                </div>
                                <div class="form-group">
                                    <label for="item-name" class="form-control-label">#Item:</label>
                                    <input type="text" class="form-control" id="item-name">
                                </div>
                                <div class="form-group">
                                    <label for="message-text" class="form-control-label">Message:</label>
                                    <textarea class="form-control" id="contact-text"></textarea>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" id="closebtn" class="btn btn-secondary"
                                        data-dismiss="modal">Close
                                </button>
                                <button type="button" id="sendbtn"
                                        class="btn btn-primary">Send message
                                </button>
                            </div>
                        </div>
                    </div>
                </div>

                <button class="btn btn-primary dropdown-toggle" type="button" id="dropdownMenuButton"
                        data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    More Options
                </button>
                <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
                    <a class="dropdown-item" data-toggle="modal"
                       data-target="#exampleModal">Report Account</a>
                </div>
                <form action="{{route('reports.storeReport')}}" method="POST" id="userReport" class="userReport">
                    {{csrf_field() }}
                    <div class="modal fade" id="exampleModal" tabindex="-1" role="dialog"
                         aria-labelledby="exampleModalLabel" aria-hidden="true">
                        <div class="modal-dialog" role="document">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title" id="exampleModalLabel">Report User Name's Account </h5>
                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                        <span aria-hidden="true">&times;</span>
                                    </button>
                                </div>
                                <div class="modal-body">

                                    <div class="form-group">
                                        <div>
                                            <h5 style="color:brown"> Motive</h5>
                                            <div class="radio">
                                                <label><input id="5" type="radio" name="radio1" value="Abusive behaviour"> Abusive behaviour </label>
                                            </div>
                                            <div class="radio">
                                                <label><input id="6" type="radio" name="radio1" value="Inappropriate content in profile">Inappropriate content in profile </label>
                                            </div>
                                            <div class="radio">
                                                <label><input id="7" type="radio" name="radio1" value="Didn't receive an item">Didn't receive an item</label>
                                            </div>
                                            <div class="radio">
                                                <label><input id="8" type="radio" name="radio1" value="FUCK OFF">Other</label>
                                            </div>
                                        </div>
                                        <label for="reason" class="col-form-label">Other:</label>
                                        <input type="text" class="form-control" id="reason1">
                                    </div>
                                    <div class="form-group">
                                        <label for="message-text" class="col-form-label">Message:</label>
                                        <textarea class="form-control" id="message-text"></textarea>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary"
                                            data-dismiss="modal">Close
                                    </button>
                                    <button type="submit" id="report" class="btn btn-primary report">Report</button>
                                </div>
                            </div>
                        </div>
                    </div>
                    @endif
                    @endif
                </form>
            </div>
        </div>
    </div>
</div>

<nav class="navbar navbar-toggleable-md navbar-light bg-faded">
    <ul class="nav nav-tabs">
        <li class="nav-item" class="active">
            <a class="nav-link active" id="details-tab" data-toggle="tab" href="#details" role="tab"
               aria-controls="details" aria-selected="true">Bid Details</a>
        </li>
        <li class="nav-item">
            <a class="nav-link" id="shippingAndPayment-tab" data-toggle="tab" href="#shippingAndPayment" role="tab"
               aria-controls="shippingAndPayment" aria-selected="true">Shipping and Payment</a>
        </li>
        <li class="nav-item">
            <a class="nav-link" id="qa-tab" data-toggle="tab" href="#qa" role="tab" aria-controls="qa"
               aria-selected="true">Q&A</a>
        </li>
        <li class="nav-item">
            <a class="nav-link" id="review-tab" data-toggle="tab" href="#reviews" role="tab" aria-controls="reviews"
               aria-selected="true">Auctioneer Reviews</a>
        </li>
    </ul>
    <button type="button" class="btn btn-danger btn-sm report" data-toggle="modal" data-target="#exampleModal1">
        Report Item
    </button>

    <form action="{{route('reports.store')}}" method="POST" id="userForm" class="userForm">
        {{csrf_field() }}
        <div class="modal fade" id="exampleModal1" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
             aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="exampleModalLabel"> Report Name's Item</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <form>
                            <div class="form-group">
                                <div>
                                    <h5 style="color:brown"> Motive</h5>
                                    <label><input id="1" type="radio" name="radio" value="Received a counterfeit or fake item"> Received a counterfeit or fake item</label>
                                    <div class="radio">
                                        <label><input id="2" type="radio" name="radio" value="This item is illegal"> This item is illegal</label>
                                    </div>
                                    <div class="radio">
                                        <label><input id="3" type="radio" name="radio" value="Received item is not in the original condition">Received item is not in the original condition</label>
                                    </div>
                                    <div class="radio">
                                        <label><input id="4" type="radio" name="radio" value="FUCK OFF">Other</label>
                                    </div>
                                </div>
                                <label for="reason" class="col-form-label">Other:</label>
                                <input type="text" class="form-control" id="reason">
                                <input type = "hidden" value={{$auction->id}} id="auction">
                                <input type = "hidden" value={{Auth::user()->id}} id="reporter">
                            </div>
                            <div class="form-group">
                                <label for="message" class="col-form-label">Message:</label>
                                <textarea class="form-control" id="message"></textarea>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                        <button type="submit" id= "sub" class="btn btn-primary">Send message</button>
                    </div>
                </div>
            </div>
        </div>
    </form>
</nav>

<!-- Tab panes -->
<div class="tab-content" id="myTabContent">
    <div class="tab-pane fade show active" id="details" role="tabpanel" aria-labelledby="details-tab">
        <dl class="row">
            <dt class="col-sm-2 text-align-center mobile-text-center">Current Bid <i class="fas fa-circle collapse collapseAlert"  title="You are currently the winner bid"></i></dt>
            <dd class="col-sm-10">1600.00€</dd>


            <dt class="col-sm-2 text-align-center mobile-text-center">Time Left</dt>
            <dd class="col-sm-10">4d 05h Wednesday, 9:15PM</dd>

            <dt class="col-sm-2 text-align-center mobile-text-center">Description</dt>
            <dd class="col-sm-10">Ultrabook with the powerful Intel i7-7700HQ Quad Core CPU with NVIDIA GTX 1050
                Gaming graphics Card.
            </dd>

            <dt class="col-sm-2 bid text-align-center mobile-text-center" id="input-bid">
                <div class="input-group mb-3">
                    <input type="number" class="form-control" aria-label="Amount (to the nearest euro)">
                    <div class="input-group-append">
                        <span class="input-group-text">€</span>
                    </div>
                </div>
            </dt>
            <dd class="col-sm-3 bid" id="button-bid">
                <div class="btn-group">
                    <a type="button" class="btn" data-toggle="collapse" href="auction_asBidder.html" data-target=".collapseAlert" id="button-bid-color">Place
                        Bid</a>
                </div>
            </dd>
        </dl>
        <div class="collapse collapseAlert">
            <div class="alert alert-success" role="alert">
                <strong>Well done!</strong> You successfully submitted a bid.
                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
        </div>
    </div>
    <div class="tab-pane fade" id="shippingAndPayment" role="tabpanel" aria-labelledby="shippingAndPayment-tab">
        <dl class="row">
            <dt class="col-sm-2 text-align-center mobile-text-center">Shipping</dt>
            <dd class="col-sm-10">FREE Economy International Shipping</dd>

            <dt class="col-sm-2 text-align-center mobile-text-center">Item location</dt>
            <dd class="col-sm-10"> Henan, China</dd>

            <dt class="col-sm-2 text-align-center mobile-text-center">Ships to</dt>
            <dd class="col-sm-10"> Worldwide</dd>

            <dt class="col-sm-2 text-align-center mobile-text-center">Delivery</dt>
            <dd class="col-sm-10"> Estimated between Fri. Mar. 23 and Tue. May. 15</dd>

            <dt class="col-sm-2 text-align-center mobile-text-center">Payments</dt>
            <dd class="col-sm-10"> Paypal | Visa | Mastercard</dd>

            <dt class="col-sm-2 text-align-center mobile-text-center">Returns</dt>
            <dd class="col-sm-10">30 day returns. Buyer pays for return shipping</dd>
        </dl>
    </div>
    <div class="tab-pane fade" id="qa" role="tabpanel" aria-labelledby="qa-tab">
        <dl class="row">
            <dt class="col-sm-4 text-align-center mobile-text-center">Does it come with a fingerprint reader?</dt>
            <dd class="col-sm-8">Nope :-)</dd>

            <dt class="col-sm-4 text-align-center mobile-text-center">
                Are you able to deliver this with portuguese keyboard layout?
            </dt>
            <dd class="col-sm-8">Yes we can delivery with Portuguese keyboard layout.</dd>
        </dl>
        <div class="input-group mb-3 col-sm-10">
            <input type="text" class="form-control" placeholder="Place your question"
                   aria-label="Place your question" aria-describedby="basic-addon2">
            <div class="input-group-append">
                <button class="btn btn-outline-secondary" type="button" data-toggle="collapse" data-target="#collapseAlertAsk">Ask</button>
            </div>

        </div>
        <div class="collapse" id="collapseAlertAsk">
            <div class="alert alert-success" role="alert">
                <strong>Well done!</strong> You successfully submitted a question.
                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
        </div>
    </div>

    <div class="tab-pane fade" id="reviews" role="tabpanel" aria-labelledby="reviews-tab">
        <div class="review-block">
            <div class="row">
                <div class="col-sm-2 text-align-center mobile-text-center">
                    <img src="https://vignette.wikia.nocookie.net/antagonist/images/8/8b/Mr._Burns.jpg"
                         class="img-review img-fluid">
                    <div class="review-block-name"><a href="../profile/profile_asUser.html">MrBurns</a></div>
                    <div class="review-block-date">March 01, 2018</div>
                </div>
                <div class="col-sm-10 ">
                    <div class="review-block-rate">
                        <span class="fas fa-star" aria-hidden="true"></span>
                        <span class="fas fa-star" aria-hidden="true"></span>
                        <span class="fas fa-star" aria-hidden="true"></span>
                        <span class="fas fa-star" aria-hidden="true"></span>
                        <span class="far fa-star" aria-hidden="true"></span>
                    </div>
                    <div class="review-block">Excellent Auctioneer, quick shipping A++++++</div>
                    <a class="badge badge-light report" type="button" data-toggle="modal"
                       href="#exampleModal">
                        Report abuse
                    </a>

                </div>
            </div>
        </div>
    </div>

</div>

<script>
    $("#sub").click(function(e){
        e.preventDefault();
        var $form = $("#userForm");
        console.log ("NAO GOSTO DISTO " );

        if (document.getElementById("1").checked) {
            var rate_value = document.getElementById("1").value;
        } else if (document.getElementById("2").checked){
            var rate_value = document.getElementById("2").value;
        } else if(document.getElementById("3").checked) {
            var rate_value = document.getElementById("3").value;
        } else if (document.getElementById("4").checked){
            var rate_value = $("#reason").val();
        }

        var message = $("#message").val();
        var url = window.location.href;
        var auctionID=url.split("/").pop();
        var userId = $("#reporter").val();

        $.ajax({
            type: $form.attr('method'),
            url: $form.attr('action'),
            data: {msg: message, sub:rate_value, user_id: userId, auction_id: auctionID, is_user: false, },
            success: function (data) {
                if(data.error){
                    return;
                }
                alert(data.success); // THis is success message
                $('#exampleModal').modal('hide');  // Your modal Id
                window.location.reload(true);
            },
            error: function () {
            }
        });
    });
</script>



<script>
    $("#report").click(function(e){
        e.preventDefault();
        var $form = $("#userReport");

        if (document.getElementById("5").checked) {
            var value = document.getElementById("5").value;
        } else if (document.getElementById("6").checked){
            var value = document.getElementById("6").value;
        } else if(document.getElementById("7").checked) {
            var value = document.getElementById("7").value;
        } else if (document.getElementById("8").checked){
            var value = $("#reason1").val();
        }

        var message = $("#message-text").val();
        var url = window.location.href;
        var reportedUser = 9;                                                         /7MUDAR ISTO

        var userId = $("#reporter").val();

        $.ajax({
            type: $form.attr('method'),
            url: $form.attr('action'),
            data: {msg: message, sub:value, user_id: userId, reported_user: reportedUser, is_user: true, },
            success: function (data) {
                if(data.error){
                    return;
                }
                alert(data.success); // THis is success message
                $('#exampleModal').modal('hide');  // Your modal Id
                window.location.reload(true);
            },
            error: function () {
            }
        });
    });
</script>
@endsection