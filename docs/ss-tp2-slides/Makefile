# Como usar: 'make all', 'make no-video' o 'make video' para generar pdf y limpiar archivos intermedios
VIDEO = video
NOVIDEO =  no-video
SOURCE = ./main.tex

all: build  clean

build: build$(NOVIDEO) build$(VIDEO) 

$(VIDEO): build$(VIDEO) clean

$(NOVIDEO): build$(NOVIDEO) clean

# Se compila dos veces para que la tabla de contenidos aparezca correctamente
build$(VIDEO): $(SOURCE)
	pdflatex -jobname=tp2-slides-$(VIDEO) '\def\OUTPUT{$(VIDEO)} \input{$(SOURCE)}' $(SOURCE) > /dev/null
	pdflatex -jobname=tp2-slides-$(VIDEO) '\def\OUTPUT{$(VIDEO)} \input{$(SOURCE)}' $(SOURCE) > /dev/null

build$(NOVIDEO) :$(SOURCE)
	pdflatex -jobname=tp2-slides-$(NOVIDEO) '\def\OUTPUT{$(NOVIDEO)} \input{$(SOURCE)}' $(SOURCE) > /dev/null
	pdflatex -jobname=tp2-slides-$(NOVIDEO) '\def\OUTPUT{$(NOVIDEO)} \input{$(SOURCE)}' $(SOURCE) > /dev/null

clean: 
	rm -rf *.aux *.log *.out *.fls *.gz *.fdb_latexmk *.snm *.toc *.vrb *.nav