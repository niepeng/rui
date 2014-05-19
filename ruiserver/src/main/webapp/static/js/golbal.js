$(function(){
	$('.pmt-account-field').mouseover(function(){
        $(this).find('.account-dropdown-menu').removeClass('hide');
	}).mouseleave(function(){
        $(this).find('.account-dropdown-menu').addClass('hide');
	});
});