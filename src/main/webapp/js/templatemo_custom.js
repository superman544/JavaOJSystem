"use strict";
var menuDisabled = false;
jQuery(document).ready(function($){

	/************** Menu Content Opening *********************/
	$(".main_menu a").on('click',function(){
		var id =  $(this).attr('class');
		id = id.split('-');
		//$("#menu-container .content").hide();
		//$("#menu-container #menu-"+id[1]).show();
		//$("#menu-container .homepage").hide();
		return false;
	});
	

	$(".main_menu a.templatemo_homeservice").click(function(){
		$("#menu-container .portfolio").hide();
		$('#menu-container .services').fadeOut(1000, function(){
        $('#menu-container .homepage').fadeIn(1000);
	    });
		return false;
	});
	
	$(".main_menu a.templatemo_page2").click(function( ){
    $('#menu-container .homepage').fadeOut(1000, function(){									  
        $('#menu-container .services').fadeIn(1000);
	    });
	return false;
	});
	
	/************** Gallery Hover Effect *********************/
	$(".overlay").hide();

	$('.gallery-item').hover(
	  function() {
	    $(this).find('.overlay').addClass('animated fadeIn').show();
	  },
	  function() {
	    $(this).find('.overlay').removeClass('animated fadeIn').hide();
	  }
	);


	/************** LightBox *********************/
	$(function(){
		$('[data-rel="lightbox"]').lightbox();
	});


	$("a.menu-toggle-btn").click(function() {
	  $(".responsive_menu").stop(true,true).slideToggle();
	  return false;
	});
 
    $(".responsive_menu a").click(function(){
		$('.responsive_menu').hide();
	});

});