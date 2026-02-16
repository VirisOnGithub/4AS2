mkdir -p public
mkdir -p public_alt
echo "<html><body><h1>Bienvenue sur le site A</h1></body></html>" > public/index.html
echo "<html><body><h1>Bienvenue sur le site B</h1></body></html>" > public_alt/index.html

javac SimpleHttpServer.java && java SimpleHttpServer 8000 &

xdg-open http://localhost:8000/ &
xdg-open http://alt.localhost:8000/ &