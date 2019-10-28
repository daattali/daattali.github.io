gadget vs addin

blog post about how to inject resources into app that need to exist only once (used in showLog and in shinyalert)

differetn ways to deploy shiny apps

what do curly braces means (on.exit, observe(), suppressstartupwarnings)

debugging shiny: options(shiny.sanitize.errors = FALSE) (`sanitize_errors false;`), `preserve_logs true`, cat, installed_packages() in UI, after seeing an error trying running "traceback()" in the command line to see the line number (only works if you can source() the app)

similar post to DO but with AWS

followup post to shiny server setup: how to develop apps/get your apps onto your server

shinymodules: what i wrote in hackfest for thibaut, plus a file input that automatically resets after upload

giving talks: number your slides so people can make note of a page and tell you where exactly to go back. live coding > code on a slide, but you have to be very familiar and comfortable with the content. dont memorize word by word, dont read off a sheet or off slides. talk to the audience. workshop: last slide is ask for feedback. presentation: last slide is quick summary (not the thank you slide!). tell them right in the beginning and remind at the end wheere they can find the content and how to contact you. 

---

media affects what we think/talk about (ebola)

always reach higher -> youll never be satisfied/happy. happiness is wanting what you have rather than having what you want, or is happiness going after what you want?

danger with social media (fb post aug 9 2015)

stop supporting 3d movies before they'll take over and we wont' have regular movies at normal prices anymore :(

natural vs unnatural products - unnatural or modified doesn't necessarily mean bad

I often see people write code like `observeEvent(input$btn, { output$plot <- renderPlot({ ... }) })` - the correct way to do this is to just place a `input$btn` expression inside the render plot. It might look weird because what can simply accessing that value without doing anything with it do? But becuase of reactivity, just using that value makes the render plot run when the button is pressed. Doing it the wrong way actually has incorrect behaviour: after pressing the button the first time, the plot will update whenever any of its reactive values change, regardless of the button. The button just needs to be clicked once. This is the exact opposite of what you're trying to do!

-----

how to write a good shiny reproducible example?  
dont use ## UI FILE .... ## SERVER FILE   
provide the sample Rmd file  
provide sample data  
make into bookmarkable app  
find exactly where the probelm is and only share that code  



Code for Clarity, not Cleverness

"Debugging is twice as hard as writing the code in the first place. Therefore, if you write the code as cleverly as possible, you are, by definition, not smart enough to debug it." - Brian Kernighan

