// data 
var dragElem = null;
function dragStartHandler(e) {
    //Set data
    dragElem = this;
    e.dataTransfer.effectAllowed = 'move';
    e.dataTransfer.setData('text/html', this.innerHTML);
    
    e.dataTransfer.setData("text", e.target.id);
	var parentId = e.target.parentElement.id;
	e.dataTransfer.setData("parent", parentId);
};
function dragOverHandler(e) {
    e.preventDefault();
    e.dataTransfer.dropEffect = 'move';
};

function dragLeaveHandler(e) {
	
};

function dragDropHandler(e) {
    e.preventDefault();
    //Get data
    //this.innerHTML = e.dataTransfer.getData('text/html');
    var data = e.dataTransfer.getData("text");
	var source = e.dataTransfer.getData("parent");
	var destination = e.target.id;
	if(((source.includes('hondaCarsListRefresingView') || source.includes('toyotaCarsListRefresingView')) && (destination === 'majorWorldContainer' || destination === 'hillsideContainer')) 
			|| ((source.includes('majorWorldCarsListRefresingView') || source.includes('hillsideCarsListRefresingView')) && (destination === 'jackContainer' || destination === 'roseContainer' || destination === 'romeoContainer' || destination === 'julietContainer'))    
			|| ((source.includes('jackCarsListRefresingView') || source.includes('roseCarsListRefresingView') || source.includes('romeoCarsListRefresingView') || source.includes('julietCarsListRefresingView')) && (destination === 'euroAutoWorksContainer' || destination === 'autoCareEastCarsContainer'))    
			|| ((source.includes('euroAutoWorksCarsListRefresingView') || source.includes('AutoCareEastCarsListRefresingView')) && (destination === 'usaaContainer' || destination === 'geicoContainer'))
			|| ((source.includes('jackCarsListRefresingView') || source.includes('roseCarsListRefresingView') || source.includes('romeoCarsListRefresingView') || source.includes('julietCarsListRefresingView')) && (destination === 'jackContainer' || destination === 'roseContainer' || destination === 'romeoContainer' || destination === 'julietContainer'))) 
	{
	    e.target.appendChild(document.getElementById(data));
	}
};

function addDragDropEventListeners ()
{
	var cols = document.querySelectorAll('.car-draggable');
	var colsLength = cols.length;
	
	// drag event lister
	for (var i = 0; i < colsLength; i++) {
	    cols[i].addEventListener('dragstart', dragStartHandler, false);
	    cols[i].addEventListener('dragover', dragOverHandler, false);
	    cols[i].addEventListener('dragleave', dragLeaveHandler, false);
	    cols[i].addEventListener('drop', dragDropHandler, false);
	};
};